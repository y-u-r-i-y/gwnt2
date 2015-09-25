package model.json;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

/**
 * Created by ysidorov on 25.09.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class BaseJsonMappingTest {
    protected ObjectMapper jsonMapper;

    @Before
    public void setUp() {
        jsonMapper = new ObjectMapper();
    }
    protected String toJson(Object object) throws IOException {
        return jsonMapper.writeValueAsString(object);
    }
    protected <T> String fromJson(Object object, Class<T> toClass) throws IOException {
        // return jsonMapper.readValue(object, toClass);
        return null;
    }


}
