package top.sinkdev.rm.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import top.sinkdev.rm.common.entity.Resident;
import top.sinkdev.rm.dao.ResidentDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ResidentDaoMySql implements ResidentDao {
    private final JdbcTemplate template;

    @Autowired
    public ResidentDaoMySql(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Resident> queryResidentList(String uid) {
        return template.query("select * from resident where uid = ?", new ResidentMapper(), uid);
    }

    @Override
    public int addResident(Resident resident) {
        return template.update("insert into resident(rid, name, gender, birthday, province, city, county, address, phone, uid, id_CN) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                resident.getRid(), resident.getName(), resident.getGender(), resident.getBirthday(), resident.getProvince(),
                resident.getCity(), resident.getCounty(), resident.getAddress(), resident.getPhone(), resident.getUid(), resident.getIdCN());
    }

    @Override
    public List<Resident> queryResidentByIDCN(String idCN, String uid) {
        return template.query("select * from resident where id_CN = ? and uid = ?",
                new ResidentMapper(), idCN, uid);
    }

    @Override
    public int updateResident(Resident resident) {
        return template.update("update resident set name=?, gender=?,birthday=?,province=?,city=?,county=?,address=?,phone=?  where id_CN = ? and uid = ?",
                resident.getName(), resident.getGender(), resident.getBirthday(), resident.getProvince(),
                resident.getCity(), resident.getCounty(), resident.getAddress(), resident.getPhone(), resident.getIdCN(), resident.getUid());
    }

    @Override
    public int deleteResident(String idCN, String uid) {
        return template.update("delete from resident where id_CN = ? and uid = ?", idCN, uid);
    }

    static class ResidentMapper extends BeanPropertyRowMapper<Resident> {
        public ResidentMapper() {
            super(Resident.class);
        }

        @Override
        public Resident mapRow(@NonNull ResultSet rs, int rowNumber) throws SQLException {
            Resident resident = super.mapRow(rs, rowNumber);
            if (resident == null) {
                return null;
            }
            resident.setIdCN(rs.getString("id_CN"));
            return resident;
        }
    }
}
