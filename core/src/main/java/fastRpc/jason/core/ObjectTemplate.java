/**    
 * 文件名：ObjectTemplate.java    
 *    
 * 版本信息：    
 * 日期：2018年3月8日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.msgpack.MessageTypeException;
import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.template.Templates;
import org.msgpack.type.ArrayValue;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;
import org.msgpack.unpacker.Unpacker;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：ObjectTemplate    
 * 类描述：    支持MessagePack
 * 创建人：jinyu    
 * 创建时间：2018年3月8日 下午6:57:23    
 * 修改人：jinyu    
 * 修改时间：2018年3月8日 下午6:57:23    
 * 修改备注：    
 * @version     
 *     
 */
public class ObjectTemplate extends AbstractTemplate<Object> {

    private ObjectTemplate() {
    }

    static public ObjectTemplate getInstance() {
        return instance;
    }

    static final ObjectTemplate instance = new ObjectTemplate();

    public void write(Packer pk, Object v, boolean required) throws IOException {
        if (v == null) {
            if (required) {
                throw new MessageTypeException("Attempted to write null");
            }
            pk.writeNil();
            return;
        }
        pk.write(v);
    }

    public Object read(Unpacker u, Object to, boolean required) throws IOException {
        if (!required && u.trySkipNil()) {
            return null;
        }

        return toObject(u.readValue());
    }

    private static Object toObject(Value value) throws IOException {
        @SuppressWarnings("resource")
        Converter conv = new Converter(value);
        if (value.isNilValue()) { // null
            return null;
        } else if (value.isRawValue()) { // byte[] or String or maybe Date?
            return conv.read(Templates.TString);
        } else if (value.isBooleanValue()) { // boolean
            return conv.read(Templates.TBoolean);
        } else if (value.isIntegerValue()) { // int or long or BigInteger
            return conv.read(Templates.TInteger);
        } else if (value.isFloatValue()) { // float or double
            return conv.read(Templates.TDouble);
        } else if (value.isArrayValue()) { // List or Set
            // deserialize value to List object
            ArrayValue v = value.asArrayValue();
            List<Object> ret = new ArrayList<Object>(v.size());
            for (Value elementValue : v) {
                ret.add(toObject(elementValue));
            }
            return ret;
        } else if (value.isMapValue()) { // Map
            MapValue v = value.asMapValue();


            Map<Object, Object> map = new HashMap<Object, Object>(v.size());
            for (Map.Entry<Value, Value> entry : v.entrySet()) {
                Value key = entry.getKey();
                Value val = entry.getValue();

                map.put(toObject(key), toObject(val));
            }

            return map;
        } else {
            throw new RuntimeException("fatal error");
        }
    }

}
