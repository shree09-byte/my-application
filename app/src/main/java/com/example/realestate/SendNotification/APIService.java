package com.example.realestate.SendNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAsaSlWY0:APA91bFo51lspJSBMW3rpLLVHN4rZ6YxGn26nuekerSNxLgy_J8nVyrBz74h777t7l6LSElGjcoiY8d_SxS4qCfkL1A07cOw2omgoipZetDqn16lavFGBsBgHoCS0VcPBB7WDhVSjwHm" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
