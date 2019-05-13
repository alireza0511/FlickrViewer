package com.jazzb.alireza.malauzai_test.Model.Flickr;

public class FlickrConstants {
    // constructor

    // getter
    // Flickr
    public String apiScheme(){
        return "https";
    }
    public String apiHost(){
        return "api.flickr.com";
    }
    public String apiPath1st(){
        return "services";
    }
    public String apiPath2nd(){
        return "rest";
    }

    // Flickr Parameter Keys
    public String parameterKeyMethod(){
            return "method";
        }
    public String parameterKeyApi(){
        return "api_key";
    }
    public String parameterKeyGalleryID(){
        return "gallery_id";
    }
    public String parameterKeyExtras(){
        return "extras";
    }
    public String parameterKeyFormat(){
        return "format";
    }
    public String parameterKeyNoJSONCallback(){
        return "nojsoncallback";
    }
    public String parameterKeyPhotosPerPage(){
        return "per_page";
    }
    public String parameterKeyPage(){
        return "page";
    }

    // Flickr Parameter Values
    public String parameterValueSearchMethod(){
        return "flickr.galleries.getPhotos";
    }
    public String parameterValueAPIKey(){
        return "edeec70474a6333489ca2522ec7b315d";
    }

    //Secret:
    //d863abe034ebb8dd
    public String parameterValueResponseFormat(){
        return "json";
    }
    public String parameterValueDisableJSONCallback(){
        return "1"; /* 1 means "yes" */
    }
    public String parameterValueGalleryPhotosMethod(){
        return "flickr.galleries.getPhotos";
    }
    public String parameterValueGalleryID(){
        return "72157706084897874";
    }
    public String parameterValueMediumURL(){
        return "url_m";
    }
    public String parameterValuePhotosPerPage(){
        return "5";
    }

    // Flickr Response Keys
    public String responseKeyStatus(){
        return "stat";
    }
    public String responseKeyPhotos(){
        return "photos";
    }
    public String responseKeyPhoto(){
        return "photo";
    }
    public String responseKeyTitle(){
        return "title";
    }
    public String responseKeyMediumURL(){
        return "url_m";
    }
    public String responseKeyPages(){
        return "pages";
    }
    public String responseKeyTotal(){
        return "total";
    }

    // Flickr Response Values
    public String responseValueOKStatus(){
        return "ok";
    }








    // setter
}

