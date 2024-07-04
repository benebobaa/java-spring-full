import com.beneboba.spring_orm.entity.Blog
import org.springframework.data.jpa.repository.JpaRepository

interface BlogRepository : JpaRepository<Blog?, Long?>
