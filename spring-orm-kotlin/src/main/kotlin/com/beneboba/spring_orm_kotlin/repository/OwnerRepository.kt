import com.beneboba.spring_orm.entity.Owner
import org.springframework.data.jpa.repository.JpaRepository

interface OwnerRepository : JpaRepository<Owner?, Long?>
