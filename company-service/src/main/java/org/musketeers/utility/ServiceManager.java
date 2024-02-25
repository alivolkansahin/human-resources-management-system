package org.musketeers.utility;

import org.musketeers.repository.CompanyRepository;
import org.musketeers.repository.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ServiceManager<T extends BaseEntity,ID> implements IService<T,ID> {

    private final JpaRepository<T,ID> jpaRepository;

    public ServiceManager(JpaRepository<T,ID> jpaRepository) {
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
        t.forEach(x-> {
            Long time = System.currentTimeMillis();
            x.setStatus(true);
            x.setCreatedAt(time);
            x.setUpdatedAt(time);
        });
        return jpaRepository.saveAll(t);
    }

    @Override
    public T update(T t) {
        t.setUpdatedAt(System.currentTimeMillis());
        return jpaRepository.save(t);
    }

    @Override
    public void delete(T t) {
        jpaRepository.delete(t);
    }

    @Override
    public void deleteById(ID id) {
    jpaRepository.deleteById(id);
    }

    @Override
    public Optional<T> findById(ID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return jpaRepository.findAll();
    }

}
