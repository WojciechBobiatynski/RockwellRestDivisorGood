UPDATE ${gryf.schema}.TI_TRAINING_INSTANCES_EXT tie
   SET TIE.START_DATE = TO_DATE (START_DATE_STR, 'YYYY-MM-DD'),
       TIE.END_DATE = TO_DATE (END_DATE_STR, 'YYYY-MM-DD'),
       TIE.PRICE = TO_NUMBER (REPLACE (PRICE_STR, ',', '.')),
       TIE.HOURS_NUMBER = TO_NUMBER (REPLACE (HOURS_NUMBER_STR, ',', '.')),
       TIE.HOUR_PRICE = TO_NUMBER (REPLACE (HOUR_PRICE_STR, ',', '.'))
 WHERE     TIE.START_DATE IS NULL
       AND TIE.END_DATE_STR IS NOT NULL
       AND TIE.END_DATE IS NULL
       AND TIE.START_DATE_STR IS NOT NULL
       AND TIE.PRICE IS NULL
       AND TIE.PRICE_STR IS NOT NULL
       AND TIE.HOURS_NUMBER IS NULL
       AND TIE.HOUR_PRICE IS NULL
;

UPDATE ${gryf.schema}.TI_TRAINING_INSTANCES_EXT_AUDT tie
   SET TIE.START_DATE = TO_DATE (START_DATE_STR, 'YYYY-MM-DD'),
       TIE.END_DATE = TO_DATE (END_DATE_STR, 'YYYY-MM-DD'),
       TIE.PRICE = TO_NUMBER (REPLACE (PRICE_STR, ',', '.')),
       TIE.HOURS_NUMBER = TO_NUMBER (REPLACE (HOURS_NUMBER_STR, ',', '.')),
       TIE.HOUR_PRICE = TO_NUMBER (REPLACE (HOUR_PRICE_STR, ',', '.'))
 WHERE     TIE.START_DATE IS NULL
       AND TIE.END_DATE_STR IS NOT NULL
       AND TIE.END_DATE IS NULL
       AND TIE.START_DATE_STR IS NOT NULL
       AND TIE.PRICE IS NULL
       AND TIE.PRICE_STR IS NOT NULL
       AND TIE.HOURS_NUMBER IS NULL
       AND TIE.HOUR_PRICE IS NULL
;