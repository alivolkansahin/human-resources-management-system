package org.musketeers.utility;

import org.musketeers.entity.BaseEntity;
import org.musketeers.exception.SpendingServiceException;
import org.musketeers.exception.ErrorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class ServiceManager<T extends BaseEntity,ID> implements IService<T,ID>{

    private final JpaRepository<T,ID> jpaRepository;

    public ServiceManager(JpaRepository<T, ID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public T save(T t) {
        Long time = System.currentTimeMillis();
        t.setCreatedAt(time);
        t.setUpdatedAt(time);
        t.setStatus(true);
        return jpaRepository.save(t);
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> t) {
        Long time = System.currentTimeMillis();
        t.forEach(x-> {
            x.setCreatedAt(time);
            x.setUpdatedAt(time);
            x.setStatus(true);
        });
        return jpaRepository.saveAll(t);
    }

    @Override
    public T update(T t) {
        t.setUpdatedAt(System.currentTimeMillis());
        return jpaRepository.save(t);
    }

    @Override
    public Boolean softDelete(T t) {
        Long time = System.currentTimeMillis();
        t.setStatus(false);
        t.setUpdatedAt(time);
        jpaRepository.save(t);
        return true;
    }

    @Override
    public void hardDelete(T t) {
        jpaRepository.delete(t);
    }

    @Override
    public Boolean softDeleteById(ID id) {
        Long time = System.currentTimeMillis();
        T t = jpaRepository.findById(id).orElseThrow(()-> new SpendingServiceException(ErrorType.SPENDING_NOT_FOUND));
        t.setStatus(false);
        t.setUpdatedAt(time);
        jpaRepository.save(t);
        return true;
    }

    @Override
    public void hardDeleteById(ID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public T findById(ID id) {
        return jpaRepository.findById(id).orElseThrow(()-> new SpendingServiceException(ErrorType.SPENDING_NOT_FOUND));
    }

    @Override
    public List<T> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Boolean existsById(ID id) {
        return jpaRepository.existsById(id);
    }

}