package com.geemeta.core.entity.parser;

import com.geemeta.core.entity.IdEntity;
import com.geemeta.core.gql.MetaManager;
import com.geemeta.core.gql.meta.EntityMeta;
import com.geemeta.core.gql.parser.CommandType;
import com.geemeta.core.gql.parser.FilterGroup;
import com.geemeta.core.gql.parser.SaveCommand;
import com.geemeta.core.mvc.Ctx;
import com.geemeta.utils.UIDGenerator;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * @author itechgee@126.com
 * @date 2017/6/21.
 */
public class EntitySaveParser {
    private MetaManager metaManager = MetaManager.singleInstance();

    public SaveCommand parse(IdEntity object, Ctx ctx) {
        EntityMeta entityMeta = metaManager.get(object.getClass());
        SaveCommand command = new SaveCommand();
        command.setEntityName(entityMeta.getEntityName());

        Map entity = null;
        try {
            entity = PropertyUtils.describe(object);
            String PK = entityMeta.getId().getFieldName();
            if (object.getId() > 0) {
                command.setCommandType(CommandType.Update);

                FilterGroup fg = new FilterGroup();
                fg.addFilter(PK, String.valueOf(entity.get(PK)));
                command.setWhere(fg);
                command.setCommandType(CommandType.Update);

                if (entity.containsKey("updateAt")) entity.put("updateAt", new Date());
                if (entity.containsKey("updater")) entity.put("updater", ctx.get("userId"));

                String[] updateFields = new String[entity.size()];
                entity.keySet().toArray(updateFields);
                command.setFields(updateFields);
                command.setValueMap(entity);
            } else {
                command.setCommandType(CommandType.Insert);
                entity.put(PK, UIDGenerator.generate(1));
                if (entity.containsKey("createAt")) entity.put("createAt", new Date());
                if (entity.containsKey("creator")) entity.put("creator", ctx.get("userId"));
                if (entity.containsKey("updateAt")) entity.put("updateAt", new Date());
                if (entity.containsKey("updater")) entity.put("updater", ctx.get("userId"));

                String[] insertFields = new String[entity.size()];
                entity.keySet().toArray(insertFields);
                command.setFields(insertFields);
                command.setValueMap(entity);
            }
            
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return command;
    }


}
