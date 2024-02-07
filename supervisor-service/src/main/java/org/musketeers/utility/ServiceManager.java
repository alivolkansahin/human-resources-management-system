package org.musketeers.utility;

import org.musketeers.entity.BaseEntity;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.SupervisorServiceException;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public class ServiceManager<T extends BaseEntity,ID> implements IService<T,ID>{

    private final MongoRepository<T,ID> mongoRepository;

    public ServiceManager(MongoRepository<T, ID> mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public T save(T t) {
        Long time = System.currentTimeMillis();
        t.setCreatedAt(time);
        t.setUpdatedAt(time);
        t.setStatus(true);
        return mongoRepository.save(t);
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> t) {
        Long time = System.currentTimeMillis();
        t.forEach(x-> {
            x.setCreatedAt(time);
            x.setUpdatedAt(time);
            x.setStatus(true);
        });
        return mongoRepository.saveAll(t);
    }

    @Override
    public T update(T t) {
        t.setUpdatedAt(System.currentTimeMillis());
        return mongoRepository.save(t);
    }

    @Override
    public Boolean softDelete(T t) {
        Long time = System.currentTimeMillis();
        t.setStatus(false);
        t.setUpdatedAt(time);
        mongoRepository.save(t);
        return true;
    }

    @Override
    public void hardDelete(T t) {
        mongoRepository.delete(t);
    }

    @Override
    public Boolean softDeleteById(ID id) {
        Long time = System.currentTimeMillis();
        T t = mongoRepository.findById(id).orElseThrow(()-> new SupervisorServiceException(ErrorType.SUPERVISOR_NOT_FOUND));
        t.setStatus(false);
        t.setUpdatedAt(time);
        mongoRepository.save(t);
        return true;
    }

    @Override
    public void hardDeleteById(ID id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public T findById(ID id) {
        return mongoRepository.findById(id).orElseThrow(()-> new SupervisorServiceException(ErrorType.SUPERVISOR_NOT_FOUND));
    }

    @Override
    public List<T> findAll() {
        return mongoRepository.findAll();
    }

    @Override
    public Boolean existsById(ID id) {
        return mongoRepository.existsById(id);
    }

}