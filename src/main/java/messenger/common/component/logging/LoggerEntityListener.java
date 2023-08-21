package messenger.common.component.logging;

import jakarta.persistence.Entity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
public class LoggerEntityListener {

    @PostPersist
    private void postPersist(Object entity) {
        Optional<Long> opt = getEntityId(entity);
        if(opt.isEmpty()) return;

        Long id = opt.orElseThrow();

        log.info("[JPA][PERSIST][ID: {}][CLASS: {}]", id, entity.getClass().getSimpleName());
    }

    @PostUpdate
    private void postUpdate(Object entity) {
        Optional<Long> opt = getEntityId(entity);
        if(opt.isEmpty()) return;

        Long id = opt.orElseThrow();

        log.info("[JPA][UPDATE][ID: {}][CLASS: {}]", id, entity.getClass().getSimpleName());
    }

    @PostRemove
    private void postRemove(Object entity) {
        Optional<Long> opt = getEntityId(entity);
        if(opt.isEmpty()) return;

        Long id = opt.orElseThrow();

        log.info("[JPA][REMOVE][ID: {}][CLASS: {}]", id, entity.getClass().getSimpleName());
    }

    private Optional<Long> getEntityId(Object entity) {

        Class<?> clazz = entity.getClass();

        if(!clazz.isAnnotationPresent(Entity.class)) return Optional.empty();

        try {

            Method method = clazz.getDeclaredMethod("getId");

            Long id = (Long) method.invoke(entity);

            return Optional.ofNullable(id);

        } catch (NoSuchMethodException e) {
            log.warn("Class {} missing getId() method", clazz.getSimpleName());
        } catch (Exception e) {
            log.error("Error while logging {} class", clazz.getSimpleName(), e);
        }

        return Optional.empty();
    }
}
