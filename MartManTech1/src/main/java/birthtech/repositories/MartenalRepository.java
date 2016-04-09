package birthtech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import birthtech.entities.Martenal;
 

@Repository
public interface MartenalRepository  extends JpaRepository<Martenal,Integer>{

}
