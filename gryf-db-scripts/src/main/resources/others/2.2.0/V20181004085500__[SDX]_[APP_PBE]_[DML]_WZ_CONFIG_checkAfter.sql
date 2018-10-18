-- Check if scripts V20181004085500__[SDX]_[APP_PBE]_[DML]_WZ_CONFIG was run !!!
SELECT
    'select ''-------------------------> Start of '
    || table_name
    || ''' as "Data of" from dual ; '
    || ' SELECT * FROM '
    || owner
    || '.'
    || table_name
    || ' where '
    || column_name
    || ' = '
    || (
        SELECT
            id
        FROM
            app_pbe.grant_programs
        WHERE
            program_code = 'WUPKKZ'
    )
    || ';'
    || 'select ''-------------------------< End of '
    || table_name
    || ''' from dual ; '
FROM
    all_tab_columns
WHERE
    owner = 'APP_PBE'
    AND column_name LIKE 'GRANT_PROGRAM_ID';