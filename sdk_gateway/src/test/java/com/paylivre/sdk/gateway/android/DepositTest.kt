package com.paylivre.sdk.gateway.android

import com.paylivre.sdk.gateway.android.domain.model.DepositStatus
import com.paylivre.sdk.gateway.android.domain.model.OriginTypeInsertProof
import org.junit.Assert
import org.junit.Test

class DepositTest {
    @Test
    fun `Test DepositStatus`() {
        Assert.assertEquals(0, DepositStatus.NEW.code)
        Assert.assertEquals(1, DepositStatus.PENDING.code)
        Assert.assertEquals(2, DepositStatus.COMPLETED.code)
        Assert.assertEquals(3, DepositStatus.CANCELLED.code)
        Assert.assertEquals(4, DepositStatus.EXPIRED.code)
        Assert.assertEquals(5, DepositStatus.REVIEWED.code)
        Assert.assertEquals(6, DepositStatus.REFUND_PENDING.code)
        Assert.assertEquals(7, DepositStatus.REFUND_COMPLETE.code)

        Assert.assertEquals(DepositStatus.NEW, DepositStatus.fromInt(0))
    }

    @Test
    fun `Test OriginTypeInsertProof`() {
        Assert.assertEquals(1, OriginTypeInsertProof.CAMERA.code)
        Assert.assertEquals(0, OriginTypeInsertProof.GALLERY.code)
    }
}