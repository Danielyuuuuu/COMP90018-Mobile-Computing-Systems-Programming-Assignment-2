package com.example.dansdistractor.vouchers;

/**
 * @ClassName: Inactivate
 * @Description:
 * @Author: wongchihaul
 * @CreateDate: 2021/10/15 11:44 下午
 */
public class InactiveVoucher extends VoucherTab {
    public InactiveVoucher() {
        super.setStatus(VoucherRecycleAdaptor.INACTIVE);
    }
}
