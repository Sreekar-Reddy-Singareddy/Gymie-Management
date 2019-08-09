package singareddy.productionapps.gymiemanagement.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import androidx.lifecycle.ViewModelProvider;

@Entity
public class Gym {

    @PrimaryKey(autoGenerate = true) private Integer _id;
    @NonNull private String name = "NA";
    @NonNull private String address = "NA";
    @NonNull private Integer pincode = 0;
    private String email;
    @NonNull private String ownerName = "NA";
    @NonNull private Long ownerMobile = 0l;

    public Gym() {

    }

    public Gym(@NonNull String name, @NonNull String address, @NonNull Integer pincode, String email, @NonNull String ownerName, @NonNull Long ownerMobile) {
        this.name = name;
        this.address = address;
        this.pincode = pincode;
        this.email = email;
        this.ownerName = ownerName;
        this.ownerMobile = ownerMobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getOwnerMobile() {
        return ownerMobile;
    }

    public void setOwnerMobile(Long ownerMobile) {
        this.ownerMobile = ownerMobile;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }
}
