package top.sinkdev.rm.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resident {
    private String rid; // 唯一标识
    private String idCN; // 中华人民共和国身份证号
    private String name;
    private String gender;
    private Date birthday;
    private String province;
    private String city;
    private String county;
    private String address;
    private String phone;

    private String uid; // user id


    public String getBirthdayStr(String format) {
        return birthday == null ? "" : new SimpleDateFormat(format, Locale.CHINA).format(birthday);
    }


}
