package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appclickup.entity.Icon;

import java.util.UUID;

@Repository
public interface IconRepository extends JpaRepository<Icon, UUID> {
}
