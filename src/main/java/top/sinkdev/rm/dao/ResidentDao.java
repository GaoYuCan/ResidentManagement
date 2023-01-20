package top.sinkdev.rm.dao;

import top.sinkdev.rm.common.entity.Resident;

import java.util.List;

public interface ResidentDao {
    List<Resident> queryResidentList(String uid);

    int addResident(Resident resident);

    List<Resident> queryResidentByIDCN(String idCN, String uid);

    int updateResident(Resident resident);

    int deleteResident(String idCN, String uid);
}
