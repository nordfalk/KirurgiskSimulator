package dk.dtu.isaacirani.kirurgisksimulator.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import dk.dtu.isaacirani.kirurgisksimulator.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton aboutus_phone;
    ImageButton aboutus_pin;
    ImageButton aboutus_mail;
    TextView phonenumber;
    TextView address;
    TextView emailaddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutus_phone = (ImageButton) findViewById(R.id.aboutus_phone);
        aboutus_pin = (ImageButton) findViewById(R.id.aboutus_pin);
        aboutus_mail = (ImageButton) findViewById(R.id.aboutus_mail);
        phonenumber = (TextView) findViewById(R.id.phonenumber);
        address = (TextView) findViewById(R.id.address);
        emailaddress = (TextView) findViewById(R.id.emailaddress);

        aboutus_mail.setOnClickListener(this);
        aboutus_pin.setOnClickListener(this);
        aboutus_phone.setOnClickListener(this);
        emailaddress.setOnClickListener(this);
        address.setOnClickListener(this);
        phonenumber.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        if(view == aboutus_mail || view == emailaddress){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "cames.rigshospital@regionh.dk" });
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(intent, ""));

        } else if (view == aboutus_pin || view == address) {
            String uri = "https://www.google.com/maps/place/Copenhagen+Academy+for+Medical+Education+and+Simulation/@55.6979566,12.5640934,17z/data=!3m1!4b1!4m5!3m4!1s0x465252ff26e39a0b:0x95e11a295631ad5d!8m2!3d55.6979536!4d12.5662821";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);

        }else if (view == aboutus_phone || view == phonenumber){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+45 35 45 54 04"));
            startActivity(intent);
        }

    }
}