package com.example.myfoodchoice.ModelFreeFoodAPI;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Dish implements Serializable
{
  private List<Meals> meals;

  public List<Meals> getMeals() {
    return this.meals;
  }

  public Dish(List<Meals> meals) {
    this.meals = meals;
  }

  public Dish()
  {

  }

  @NonNull
  @Override
  public String toString()
  {
    return "Dish{" +
            "meals=" + meals +
            '}';
  }

  public void setMeals(List<Meals> meals) {
    this.meals = meals;
  }

  public static class Meals implements Serializable
  {
    private Object strImageSource;

    private String strIngredient10;

    private String strIngredient12;

    private String strIngredient11;

    private String strIngredient14;

    private String strCategory;

    private String strIngredient13;

    private String strIngredient16;

    private String strIngredient15;

    private String strIngredient18;

    private String strIngredient17;

    private String strArea;

    private Object strCreativeCommonsConfirmed;

    private String strIngredient19;

    private Object strTags;

    private String idMeal;

    private String strInstructions;

    private String strIngredient1;

    private String strIngredient3;

    private String strIngredient2;

    private String strIngredient20;

    private String strIngredient5;

    private String strIngredient4;

    private String strIngredient7;

    private String strIngredient6;

    private String strIngredient9;

    private String strIngredient8;

    private String strMealThumb;

    private String strMeasure20;

    private String strYoutube;

    private String strMeal;

    private String strMeasure12;

    private String strMeasure13;

    private String strMeasure10;

    private String strMeasure11;

    private Object dateModified;

    private Object strDrinkAlternate;

    private String strSource;

    private String strMeasure9;

    private String strMeasure7;

    private String strMeasure8;

    private String strMeasure5;

    private String strMeasure6;

    private String strMeasure3;

    private String strMeasure4;

    private String strMeasure1;

    private String strMeasure18;

    private String strMeasure2;

    private String strMeasure19;

    private String strMeasure16;

    private String strMeasure17;

    private String strMeasure14;

    private String strMeasure15;

    @NonNull
    @Override
    public String toString()
    {
      return "Meals{" +
              "strImageSource=" + strImageSource +
              ", strIngredient10='" + strIngredient10 + '\'' +
              ", strIngredient12='" + strIngredient12 + '\'' +
              ", strIngredient11='" + strIngredient11 + '\'' +
              ", strIngredient14='" + strIngredient14 + '\'' +
              ", strCategory='" + strCategory + '\'' +
              ", strIngredient13='" + strIngredient13 + '\'' +
              ", strIngredient16='" + strIngredient16 + '\'' +
              ", strIngredient15='" + strIngredient15 + '\'' +
              ", strIngredient18='" + strIngredient18 + '\'' +
              ", strIngredient17='" + strIngredient17 + '\'' +
              ", strArea='" + strArea + '\'' +
              ", strCreativeCommonsConfirmed=" + strCreativeCommonsConfirmed +
              ", strIngredient19='" + strIngredient19 + '\'' +
              ", strTags=" + strTags +
              ", idMeal='" + idMeal + '\'' +
              ", strInstructions='" + strInstructions + '\'' +
              ", strIngredient1='" + strIngredient1 + '\'' +
              ", strIngredient3='" + strIngredient3 + '\'' +
              ", strIngredient2='" + strIngredient2 + '\'' +
              ", strIngredient20='" + strIngredient20 + '\'' +
              ", strIngredient5='" + strIngredient5 + '\'' +
              ", strIngredient4='" + strIngredient4 + '\'' +
              ", strIngredient7='" + strIngredient7 + '\'' +
              ", strIngredient6='" + strIngredient6 + '\'' +
              ", strIngredient9='" + strIngredient9 + '\'' +
              ", strIngredient8='" + strIngredient8 + '\'' +
              ", strMealThumb='" + strMealThumb + '\'' +
              ", strMeasure20='" + strMeasure20 + '\'' +
              ", strYoutube='" + strYoutube + '\'' +
              ", strMeal='" + strMeal + '\'' +
              ", strMeasure12='" + strMeasure12 + '\'' +
              ", strMeasure13='" + strMeasure13 + '\'' +
              ", strMeasure10='" + strMeasure10 + '\'' +
              ", strMeasure11='" + strMeasure11 + '\'' +
              ", dateModified=" + dateModified +
              ", strDrinkAlternate=" + strDrinkAlternate +
              ", strSource='" + strSource + '\'' +
              ", strMeasure9='" + strMeasure9 + '\'' +
              ", strMeasure7='" + strMeasure7 + '\'' +
              ", strMeasure8='" + strMeasure8 + '\'' +
              ", strMeasure5='" + strMeasure5 + '\'' +
              ", strMeasure6='" + strMeasure6 + '\'' +
              ", strMeasure3='" + strMeasure3 + '\'' +
              ", strMeasure4='" + strMeasure4 + '\'' +
              ", strMeasure1='" + strMeasure1 + '\'' +
              ", strMeasure18='" + strMeasure18 + '\'' +
              ", strMeasure2='" + strMeasure2 + '\'' +
              ", strMeasure19='" + strMeasure19 + '\'' +
              ", strMeasure16='" + strMeasure16 + '\'' +
              ", strMeasure17='" + strMeasure17 + '\'' +
              ", strMeasure14='" + strMeasure14 + '\'' +
              ", strMeasure15='" + strMeasure15 + '\'' +
              '}';
    }

    public Object getStrImageSource() {
      return this.strImageSource;
    }

    public void setStrImageSource(Object strImageSource) {
      this.strImageSource = strImageSource;
    }

    public String getStrIngredient10() {
      return this.strIngredient10;
    }

    public void setStrIngredient10(String strIngredient10) {
      this.strIngredient10 = strIngredient10;
    }

    public String getStrIngredient12() {
      return this.strIngredient12;
    }

    public void setStrIngredient12(String strIngredient12) {
      this.strIngredient12 = strIngredient12;
    }

    public String getStrIngredient11() {
      return this.strIngredient11;
    }

    public void setStrIngredient11(String strIngredient11) {
      this.strIngredient11 = strIngredient11;
    }

    public String getStrIngredient14() {
      return this.strIngredient14;
    }

    public void setStrIngredient14(String strIngredient14) {
      this.strIngredient14 = strIngredient14;
    }

    public String getStrCategory() {
      return this.strCategory;
    }

    public void setStrCategory(String strCategory) {
      this.strCategory = strCategory;
    }

    public String getStrIngredient13() {
      return this.strIngredient13;
    }

    public void setStrIngredient13(String strIngredient13) {
      this.strIngredient13 = strIngredient13;
    }

    public String getStrIngredient16() {
      return this.strIngredient16;
    }

    public void setStrIngredient16(String strIngredient16) {
      this.strIngredient16 = strIngredient16;
    }

    public String getStrIngredient15() {
      return this.strIngredient15;
    }

    public void setStrIngredient15(String strIngredient15) {
      this.strIngredient15 = strIngredient15;
    }

    public String getStrIngredient18() {
      return this.strIngredient18;
    }

    public void setStrIngredient18(String strIngredient18) {
      this.strIngredient18 = strIngredient18;
    }

    public String getStrIngredient17() {
      return this.strIngredient17;
    }

    public void setStrIngredient17(String strIngredient17) {
      this.strIngredient17 = strIngredient17;
    }

    public String getStrArea() {
      return this.strArea;
    }

    public void setStrArea(String strArea) {
      this.strArea = strArea;
    }

    public Object getStrCreativeCommonsConfirmed() {
      return this.strCreativeCommonsConfirmed;
    }

    public void setStrCreativeCommonsConfirmed(Object strCreativeCommonsConfirmed) {
      this.strCreativeCommonsConfirmed = strCreativeCommonsConfirmed;
    }

    public String getStrIngredient19() {
      return this.strIngredient19;
    }

    public void setStrIngredient19(String strIngredient19) {
      this.strIngredient19 = strIngredient19;
    }

    public Object getStrTags() {
      return this.strTags;
    }

    public void setStrTags(Object strTags) {
      this.strTags = strTags;
    }

    public String getIdMeal() {
      return this.idMeal;
    }

    public void setIdMeal(String idMeal) {
      this.idMeal = idMeal;
    }

    public String getStrInstructions() {
      return this.strInstructions;
    }

    public void setStrInstructions(String strInstructions) {
      this.strInstructions = strInstructions;
    }

    public String getStrIngredient1() {
      return this.strIngredient1;
    }

    public void setStrIngredient1(String strIngredient1) {
      this.strIngredient1 = strIngredient1;
    }

    public String getStrIngredient3() {
      return this.strIngredient3;
    }

    public void setStrIngredient3(String strIngredient3) {
      this.strIngredient3 = strIngredient3;
    }

    public String getStrIngredient2() {
      return this.strIngredient2;
    }

    public void setStrIngredient2(String strIngredient2) {
      this.strIngredient2 = strIngredient2;
    }

    public String getStrIngredient20() {
      return this.strIngredient20;
    }

    public void setStrIngredient20(String strIngredient20) {
      this.strIngredient20 = strIngredient20;
    }

    public String getStrIngredient5() {
      return this.strIngredient5;
    }

    public void setStrIngredient5(String strIngredient5) {
      this.strIngredient5 = strIngredient5;
    }

    public String getStrIngredient4() {
      return this.strIngredient4;
    }

    public void setStrIngredient4(String strIngredient4) {
      this.strIngredient4 = strIngredient4;
    }

    public String getStrIngredient7() {
      return this.strIngredient7;
    }

    public void setStrIngredient7(String strIngredient7) {
      this.strIngredient7 = strIngredient7;
    }

    public String getStrIngredient6() {
      return this.strIngredient6;
    }

    public void setStrIngredient6(String strIngredient6) {
      this.strIngredient6 = strIngredient6;
    }

    public String getStrIngredient9() {
      return this.strIngredient9;
    }

    public void setStrIngredient9(String strIngredient9) {
      this.strIngredient9 = strIngredient9;
    }

    public String getStrIngredient8() {
      return this.strIngredient8;
    }

    public void setStrIngredient8(String strIngredient8) {
      this.strIngredient8 = strIngredient8;
    }

    public String getStrMealThumb() {
      return this.strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
      this.strMealThumb = strMealThumb;
    }

    public String getStrMeasure20() {
      return this.strMeasure20;
    }

    public void setStrMeasure20(String strMeasure20) {
      this.strMeasure20 = strMeasure20;
    }

    public String getStrYoutube() {
      return this.strYoutube;
    }

    public void setStrYoutube(String strYoutube) {
      this.strYoutube = strYoutube;
    }

    public String getStrMeal() {
      return this.strMeal;
    }

    public void setStrMeal(String strMeal) {
      this.strMeal = strMeal;
    }

    public String getStrMeasure12() {
      return this.strMeasure12;
    }

    public void setStrMeasure12(String strMeasure12) {
      this.strMeasure12 = strMeasure12;
    }

    public String getStrMeasure13() {
      return this.strMeasure13;
    }

    public void setStrMeasure13(String strMeasure13) {
      this.strMeasure13 = strMeasure13;
    }

    public String getStrMeasure10() {
      return this.strMeasure10;
    }

    public void setStrMeasure10(String strMeasure10) {
      this.strMeasure10 = strMeasure10;
    }

    public String getStrMeasure11() {
      return this.strMeasure11;
    }

    public void setStrMeasure11(String strMeasure11) {
      this.strMeasure11 = strMeasure11;
    }

    public Object getDateModified() {
      return this.dateModified;
    }

    public void setDateModified(Object dateModified) {
      this.dateModified = dateModified;
    }

    public Object getStrDrinkAlternate() {
      return this.strDrinkAlternate;
    }

    public void setStrDrinkAlternate(Object strDrinkAlternate) {
      this.strDrinkAlternate = strDrinkAlternate;
    }

    public String getStrSource() {
      return this.strSource;
    }

    public void setStrSource(String strSource) {
      this.strSource = strSource;
    }

    public String getStrMeasure9() {
      return this.strMeasure9;
    }

    public void setStrMeasure9(String strMeasure9) {
      this.strMeasure9 = strMeasure9;
    }

    public String getStrMeasure7() {
      return this.strMeasure7;
    }

    public void setStrMeasure7(String strMeasure7) {
      this.strMeasure7 = strMeasure7;
    }

    public String getStrMeasure8() {
      return this.strMeasure8;
    }

    public void setStrMeasure8(String strMeasure8) {
      this.strMeasure8 = strMeasure8;
    }

    public String getStrMeasure5() {
      return this.strMeasure5;
    }

    public void setStrMeasure5(String strMeasure5) {
      this.strMeasure5 = strMeasure5;
    }

    public String getStrMeasure6() {
      return this.strMeasure6;
    }

    public void setStrMeasure6(String strMeasure6) {
      this.strMeasure6 = strMeasure6;
    }

    public String getStrMeasure3() {
      return this.strMeasure3;
    }

    public void setStrMeasure3(String strMeasure3) {
      this.strMeasure3 = strMeasure3;
    }

    public String getStrMeasure4() {
      return this.strMeasure4;
    }

    public void setStrMeasure4(String strMeasure4) {
      this.strMeasure4 = strMeasure4;
    }

    public String getStrMeasure1() {
      return this.strMeasure1;
    }

    public void setStrMeasure1(String strMeasure1) {
      this.strMeasure1 = strMeasure1;
    }

    public String getStrMeasure18() {
      return this.strMeasure18;
    }

    public void setStrMeasure18(String strMeasure18) {
      this.strMeasure18 = strMeasure18;
    }

    public String getStrMeasure2() {
      return this.strMeasure2;
    }

    public void setStrMeasure2(String strMeasure2) {
      this.strMeasure2 = strMeasure2;
    }

    public String getStrMeasure19() {
      return this.strMeasure19;
    }

    public void setStrMeasure19(String strMeasure19) {
      this.strMeasure19 = strMeasure19;
    }

    public String getStrMeasure16() {
      return this.strMeasure16;
    }

    public void setStrMeasure16(String strMeasure16) {
      this.strMeasure16 = strMeasure16;
    }

    public String getStrMeasure17() {
      return this.strMeasure17;
    }

    public void setStrMeasure17(String strMeasure17) {
      this.strMeasure17 = strMeasure17;
    }

    public String getStrMeasure14() {
      return this.strMeasure14;
    }

    public void setStrMeasure14(String strMeasure14) {
      this.strMeasure14 = strMeasure14;
    }

    public String getStrMeasure15() {
      return this.strMeasure15;
    }

    public void setStrMeasure15(String strMeasure15) {
      this.strMeasure15 = strMeasure15;
    }
  }
}
