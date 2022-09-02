package cinema.dao.impl;

import cinema.dao.AbstractDao;
import cinema.dao.RoleDao;
import cinema.exception.DataProcessingException;
import cinema.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {
    private static final Logger LOGGER = LogManager.getLogger(RoleDaoImpl.class);

    public RoleDaoImpl(SessionFactory factory) {
        super(factory, Role.class);
    }

    @Override
    public Role getByName(String roleName) {
        try (Session session = factory.openSession()) {
            Query<Role> query =
                    session.createQuery("FROM Role WHERE roleName = :roleName", Role.class);
            query.setParameter("roleName", Role.RoleName.valueOf(roleName));
            LOGGER.info("Got role, role name:{}", roleName);
            return query.getSingleResult();
        } catch (Exception e) {
            LOGGER.error("Can't get role, role name:{}", roleName);
            throw new DataProcessingException("Role " + roleName + " not found", e);
        }
    }
}
