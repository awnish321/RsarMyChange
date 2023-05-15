package rsarapp.com.modelClass.request;

public class DeviceDetailModel {
    private String Device_Id;
    private String Mob_Id;
    private String Mob_Product;
    private String Mob_Brand;
    private String Mob_Manufacture;
    private String Mob_Model;

    public String getDevice_Id() {
        return Device_Id;
    }

    public DeviceDetailModel(String device_Id, String mob_Id, String mob_Product, String mob_Brand, String mob_Manufacture, String mob_Model) {
        Device_Id = device_Id;
        Mob_Id = mob_Id;
        Mob_Product = mob_Product;
        Mob_Brand = mob_Brand;
        Mob_Manufacture = mob_Manufacture;
        Mob_Model = mob_Model;
    }

    public void setDevice_Id(String device_Id) {
        Device_Id = device_Id;
    }

    public String getMob_Id() {
        return Mob_Id;
    }

    public void setMob_Id(String mob_Id) {
        Mob_Id = mob_Id;
    }

    public String getMob_Product() {
        return Mob_Product;
    }

    public void setMob_Product(String mob_Product) {
        Mob_Product = mob_Product;
    }

    public String getMob_Brand() {
        return Mob_Brand;
    }

    public void setMob_Brand(String mob_Brand) {
        Mob_Brand = mob_Brand;
    }

    public String getMob_Manufacture() {
        return Mob_Manufacture;
    }

    public void setMob_Manufacture(String mob_Manufacture) {
        Mob_Manufacture = mob_Manufacture;
    }

    public String getMob_Model() {
        return Mob_Model;
    }

    public void setMob_Model(String mob_Model) {
        Mob_Model = mob_Model;
    }
}
