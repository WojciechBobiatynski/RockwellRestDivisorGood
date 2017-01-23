CREATE OR REPLACE PACKAGE EAGLE.PK_GRF_UTILS AS
/******************************************************************************
     NAME:       PK_GRF_UTILS
     PURPOSE:    PLSQL Utils for GRYF WEBAPP

     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     -1.0       2015-09-01  MGU              Cratered
     -0.9       2016-12-07  MAPO             Cratered deeper. WUP debit note creation
   -0.8       2016-12-30  MAPO             WUP Credit Note

******************************************************************************/
  FUNCTION Amount_Verbally(a_amount NUMBER) RETURN VARCHAR2;

  FUNCTION Next_Business_Day(a_date DATE) RETURN DATE;

  -- funckja zwraca nty dzieñ roboczy od podatej daty
  FUNCTION Get_Nth_Business_Day(a_date DATE, a_nth_day NUMBER) RETURN DATE;

  -- funckja zwraca nty dzieñ kalendarzowy od podatej daty
  FUNCTION Get_Nth_Callendar_Day(a_date DATE, a_nth_day NUMBER) RETURN DATE;

  -- funckja zwraca nty dzieñ roboczy lub kalendarzowy od podatej daty
  -- a_day_type: B - roboczy (BUISNESS), C - kalendarzowy (CALENDAR)
  FUNCTION Get_Nth_Day(a_date DATE, a_nth_day NUMBER, a_day_type VARCHAR2) RETURN DATE;


  -- utwórz notê obci¹¿eniowo-ksiêgow¹ dla Uczestnika (beneficjenta)
    PROCEDURE Create_Pb_Cus_Note( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, o_87_invoice_number OUT VARCHAR2, a_order_id IN NUMBER, a_sign_user IN VARCHAR2 DEFAULT USER);

  -- utwórz notê uznaniow¹ dla uczestnika
    PROCEDURE Create_Pb_Rmb_Note( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, a_ermb_id IN NUMBER, a_sign_user IN VARCHAR2 DEFAULT USER, a_type in VARCHAR2 default 'CUS' );



END PK_GRF_UTILS;
/