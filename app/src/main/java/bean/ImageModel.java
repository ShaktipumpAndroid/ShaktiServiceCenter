package bean;

public class ImageModel {

    String ID,name,ImagePath,billNo;
    boolean isImageSelected, boerwellLiffiting, borewellLowering, transportLoading, transportUnLoading;


    public boolean isBoerwellLiffiting() {
        return boerwellLiffiting;
    }

    public void setBoerwellLiffiting(boolean boerwellLiffiting) {
        this.boerwellLiffiting = boerwellLiffiting;
    }

    public boolean isBorewellLowering() {
        return borewellLowering;
    }

    public void setBorewellLowering(boolean borewellLowering) {
        this.borewellLowering = borewellLowering;
    }

    public boolean isTransportLoading() {
        return transportLoading;
    }

    public void setTransportLoading(boolean transportLoading) {
        this.transportLoading = transportLoading;
    }

    public boolean isTransportUnLoading() {
        return transportUnLoading;
    }

    public void setTransportUnLoading(boolean transportUnLoading) {
        this.transportUnLoading = transportUnLoading;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public boolean isImageSelected() {
        return isImageSelected;
    }

    public void setImageSelected(boolean imageSelected) {
        isImageSelected = imageSelected;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

