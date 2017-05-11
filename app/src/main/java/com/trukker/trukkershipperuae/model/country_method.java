package com.trukker.trukkershipperuae.model;

public class country_method
{
	 private String number;
	  private String name;
	 
	  public country_method(String num, String nam)
	  {
	   number = num;
	   name = nam;
	  }
	 
	  public String getNumber() {
	   return number;
	  }
	 
	  public String getName() {
	   return name;
	  }
}

 /* viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) 
            {
               Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show(); 
               Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show(); 
               
               
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

*/