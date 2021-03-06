merge into APP_PBE.TI_TRAINING_INSTANCES tti
using ( 
    SELECT distinct ti.id, e.IND_ORDER_EXTERNAL_ID
    FROM APP_PBE.TI_TRAINING_INSTANCES ti
    join APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES pipu on pipu.TRAINING_INSTANCE_ID=ti.id
    join APP_PBE.PBE_PRODUCT_INSTANCE_POOLS pip on pip.ID=pipu.PRODUCT_INSTANCE_POOL_ID
    join APP_PBE.ORDERS o on o.id=pip.ORDER_ID
    join APP_PBE.CONTRACTS c on c.ID=o.CONTRACT_ID
    join APP_PBE.TI_TRAINING_INSTANCES_EXT e on e.TRAINING_ID=ti.TRAINING_ID and e.IND_ORDER_EXTERNAL_ID like '%/'|| c.ID ||'/1'
) t
on (tti.id=t.id)
when matched then update set
tti.IND_ORDER_EXTERNAL_ID = t.IND_ORDER_EXTERNAL_ID