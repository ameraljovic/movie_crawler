package ba.aljovic.amer.component;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;

public class HibernateExtendedJpaDialect extends HibernateJpaDialect
{
    /**
     * This method is overridden to set custom isolation levels on the connection
     *
     * @param entityManager
     * @param definition
     * @return
     * @throws PersistenceException
     * @throws SQLException
     * @throws TransactionException
     */
    @Override
    public Object beginTransaction(final EntityManager entityManager, final TransactionDefinition definition)
            throws PersistenceException, SQLException, TransactionException
    {
        Session session = (Session)entityManager.getDelegate();
        if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT)
        {
            getSession(entityManager).getTransaction().setTimeout(definition.getTimeout());
        }

        entityManager.getTransaction().begin();

        session.doWork(new Work()
        {

            public void execute(Connection connection) throws SQLException
            {
                DataSourceUtils.prepareConnectionForTransaction(connection, definition);
            }
        });

        return prepareTransaction(entityManager, definition.isReadOnly(), definition.getName());
    }
}
