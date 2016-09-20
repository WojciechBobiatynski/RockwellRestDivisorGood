package pl.sodexo.it.gryf.model;

/**
 * Enum przechowuje niektóre parametry raportów - m.in. parametry standardowe
 * @author Marcel.GOLUNSKI
 */
public enum ReportParameter{
    /**
     * Ścieżka do grafik dla raportów
     */
    IMAGES_PATH("imagesPath"), 
    /**
     * parametr id zamówienia
     */
    ORDER_ID("orderId");

    //FIELDS

    private String param;

    //CONSTRUCTORS

    private ReportParameter(String param){
        this.param = param;
    }

    //GETTERS

    public String getParam() {
        return param;
    }


}
