package org.musketeers.utility;

import org.musketeers.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ServiceManager<T extends BaseEntity,ID> implements IService<T,ID> {

    private final JpaRepository<T,ID> jpaRepository;

    public ServiceManager(JpaRepository<T, ID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public T save(T t) {
        LocalDate date= LocalDate.now();
        t.setCreateAt(date);
        t.setUpdateAt(date);
        t.setState(true);
        return jpaRepository.save(t);
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> t) {
        LocalDate date= LocalDate.now();
        t.forEach(x->{
            x.setCreateAt(date);
            x.setUpdateAt(date);
            x.setState(true);
        });

        return jpaRepository.saveAll(t);
    }

    @Override
    public T update(T t) {
        t.setUpdateAt(LocalDate.now());
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
