package top.sinkdev.rm.service;

import org.springframework.lang.NonNull;
import top.sinkdev.rm.common.SimpleResponse;
import top.sinkdev.rm.common.entity.Resident;

import java.util.List;

public interface HomeService {
    SimpleResponse<List<Resident>> getResidentList(@NonNull String uid);

    SimpleResponse<Resident> addResident(@NonNull Resident resident, @NonNull String uid);

    SimpleResponse<Resident> getResident(@NonNull String idCN, @NonNull String uid);


    SimpleResponse<Resident> updateResident(@NonNull Resident resident, @NonNull String uid);

    SimpleResponse<Boolean> deleteResident(@NonNull String idCN, @NonNull String uid);


}
