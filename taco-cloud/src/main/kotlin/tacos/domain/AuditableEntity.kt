package tacos.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class AuditableEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    var createdDate: Date? = null

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    var updatedDate: Date? = null

}
