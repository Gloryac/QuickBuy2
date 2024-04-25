package com.example.quickbuy2.Domain;

/*public class SliderItems {
    private static String sliderImage;
    public SliderItems() {
    }
    public SliderItems(String url){
        this.url=url;
    }
    public static String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
*/
public class SliderItems {
    private String sliderImage;
    // Default constructor required for calls to DataSnapshot.getValue(SliderItems.class)
    public SliderItems() {
    }
    public SliderItems(String sliderImage) {
        this.sliderImage = sliderImage;
    }
    public String getSliderImage() {
        return sliderImage;
    }
    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }
}

