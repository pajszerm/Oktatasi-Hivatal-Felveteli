package com.example.felveteli.repositories;

import com.example.felveteli.domain.Representative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepresentativeRepository extends JpaRepository<Representative, Long> {

    @Query("SELECT r FROM Representative r WHERE r.name = :name")
    Representative findRepresentativeByName(@Param("name") String name);
}
