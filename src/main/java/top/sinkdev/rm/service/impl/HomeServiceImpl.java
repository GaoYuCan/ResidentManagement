package top.sinkdev.rm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import top.sinkdev.rm.common.SimpleResponse;
import top.sinkdev.rm.common.entity.Resident;
import top.sinkdev.rm.dao.ResidentDao;
import top.sinkdev.rm.service.HomeService;

import java.util.List;
import java.util.UUID;

@Service
public class HomeServiceImpl implements HomeService {

    private final ResidentDao residentDao;

    @Autowired
    public HomeServiceImpl(ResidentDao residentDao) {
        this.residentDao = residentDao;
    }

    @Override
    public SimpleResponse<List<Resident>> getResidentList(@NonNull String uid) {
        List<Resident> residents = residentDao.queryResidentList(uid);
        if (residents == null) {
            return SimpleResponse.createFailureResponse("获取列表失败，未知原因！");
        }
        return SimpleResponse.createSuccessResponse(residents, "获取成功！");
    }

    @Override
    public SimpleResponse<Resident> addResident(@NonNull Resident resident, @NonNull String uid) {
        // generate and set rid
        String rid = UUID.randomUUID().toString().replace("-", "");
        resident.setRid(rid);
        resident.setUid(uid);
        // query conflict information
        List<Resident> residents = residentDao.queryResidentByIDCN(resident.getIdCN(), resident.getUid());
        if (residents != null && !residents.isEmpty()) {
            return SimpleResponse.createFailureResponse("添加失败，该身份证号已存在！");
        }
        // insert into database
        int i = residentDao.addResident(resident);
        if (i == 1) {
            return SimpleResponse.createSuccessResponse(resident, "添加成功！");
        }
        return SimpleResponse.createFailureResponse("添加失败，原因未知");
    }

    @Override
    public SimpleResponse<Resident> getResident(@NonNull String idCN, @NonNull String uid) {
        List<Resident> residents = residentDao.queryResidentByIDCN(idCN, uid);
        if (residents == null || residents.isEmpty()) {
            return SimpleResponse.createFailureResponse("获取失败！");
        }
        return SimpleResponse.createSuccessResponse(residents.get(0), "获取成功！");
    }

    @Override
    public SimpleResponse<Resident> updateResident(@NonNull Resident resident, @NonNull String uid) {
        resident.setUid(uid);
        int i = residentDao.updateResident(resident);
        if (i == 1) {
            return SimpleResponse.createSuccessResponse(resident, "更新成功！");
        }
        return SimpleResponse.createFailureResponse("更新失败，未知原因！");
    }

    @Override
    public SimpleResponse<Boolean> deleteResident(@NonNull String idCN, @NonNull String uid) {
        int i = residentDao.deleteResident(idCN, uid);
        if (i == 1) {
            return SimpleResponse.createSuccessResponse(true, "删除成功!");
        }
        return SimpleResponse.createSuccessResponse(false, "删除失败，未知原因!");
    }
}
