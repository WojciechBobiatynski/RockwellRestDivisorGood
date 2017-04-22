update APP_PBE.PBE_PRODUCT_INSTANCES ppi
set ppi.ORDER_ID = (select pool.ORDER_ID from APP_PBE.PBE_PRODUCT_INSTANCE_POOLS pool where pool.ID = ppi.PRODUCT_INSTANCE_POOL_ID)
where ppi.PRODUCT_INSTANCE_POOL_ID is not null and ppi.order_id is null;