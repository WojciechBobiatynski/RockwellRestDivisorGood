package pl.sodexo.it.gryf.common.dto;

/**
 * Created by tomasz.bilski.ext on 2015-09-16.
 */
public enum YesNo {

    Y(true),N(false);

    private boolean flag;

    YesNo(boolean flag){
        this.flag = flag;
    }

    //PUBLIC METHODS

    public static YesNo fromBoolean(Boolean value){
        if(value == null){
            return null;
        }
        return value ? Y : N;
    }

    public static Boolean toBoolean(YesNo yesNo){
        if(yesNo == null){
            return null;
        }
        return yesNo.toBoolean();
    }

    public boolean toBoolean(){
        return flag;
    }
}
