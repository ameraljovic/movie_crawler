package ba.aljovic.amer.application.batch.chunk;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class RangePartitioner implements Partitioner
{
    public static final String FROM_ID = "fromId";
    public static final String TO_ID = "toId";
    private Integer fromId;
    private Integer range;

    public RangePartitioner(Integer fromId, Integer range)
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
            value.putInt(FROM_ID, fromId);
            value.putInt(TO_ID, toId);
            value.putString("name", "Thread" + i);
            result.put("partition" + i, value);

            fromId += range;
        }
        return result;
    }
}
