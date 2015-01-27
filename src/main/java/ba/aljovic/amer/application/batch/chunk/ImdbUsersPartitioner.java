package ba.aljovic.amer.application.batch.chunk;

import ba.aljovic.amer.application.utils.Utils;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class ImdbUsersPartitioner implements Partitioner
{
    private Integer fromId;
    private Integer range;

    public ImdbUsersPartitioner(Integer fromId, Integer range)
    {
        this.fromId = fromId;
        this.range = range;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize)
    {
        assert gridSize > 0;
        Map<String, ExecutionContext> result = new HashMap<>();
        for (int i = 1; i <= gridSize; i++)
        {
            Integer toId = fromId + range - 1;

            ExecutionContext value = new ExecutionContext();
            value.putInt(Utils.FROM_ID, fromId);
            value.putInt(Utils.TO_ID, toId);
            value.putString("name", "Thread" + i);
            result.put("partition" + i, value);

            fromId += range;
        }
        return result;
    }
}
