// RecipeResponse.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.myfoodchoice.ModelEdamam;
import java.util.List;

public class RecipeResponse {
    private List<Hit> hits;
    private Links links;
    private long count;
    private long from;
    private long to;

    public List<Hit> getHits() { return hits; }
    public void setHits(List<Hit> value) { this.hits = value; }

    public Links getLinks() { return links; }
    public void setLinks(Links value) { this.links = value; }

    public long getCount() { return count; }
    public void setCount(long value) { this.count = value; }

    public long getFrom() { return from; }
    public void setFrom(long value) { this.from = value; }

    public long getTo() { return to; }
    public void setTo(long value) { this.to = value; }
}

// Hit.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Hit {
    private Links links;
    private Recipe recipe;

    public Links getLinks() { return links; }
    public void setLinks(Links value) { this.links = value; }

    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe value) { this.recipe = value; }
}

// Links.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Links {
    private Next next;
    private Next self;

    public Next getNext() { return next; }
    public void setNext(Next value) { this.next = value; }

    public Next getSelf() { return self; }
    public void setSelf(Next value) { this.self = value; }
}

// Next.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Next {
    private String href;
    private String title;

    public String getHref() { return href; }
    public void setHref(String value) { this.href = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }
}

// Recipe.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Recipe {
    private String shareAs;
    private List<String> instructions;
    private String co2EmissionsClass;
    private List<String> mealType;
    private String source;
    private long totalCO2Emissions;
    private List<String> cuisineType;
    private Total totalNutrients;
    private long inflammatoryIndex;
    private long yield;
    private List<Digest> digest;
    private List<Ingredient> ingredients;
    private Total totalDaily;
    private List<String> ingredientLines;
    private String image;
    private Images images;
    private List<String> cautions;
    private List<String> healthLabels;
    private String externalId;
    private String label;
    private long calories;
    private String uri;
    private String url;
    private List<String> tags;
    private List<String> dietLabels;
    private List<String> dishType;
    private long totalWeight;
    private long glycemicIndex;

    public String getShareAs() { return shareAs; }
    public void setShareAs(String value) { this.shareAs = value; }

    public List<String> getInstructions() { return instructions; }
    public void setInstructions(List<String> value) { this.instructions = value; }

    public String getCo2EmissionsClass() { return co2EmissionsClass; }
    public void setCo2EmissionsClass(String value) { this.co2EmissionsClass = value; }

    public List<String> getMealType() { return mealType; }
    public void setMealType(List<String> value) { this.mealType = value; }

    public String getSource() { return source; }
    public void setSource(String value) { this.source = value; }

    public long getTotalCO2Emissions() { return totalCO2Emissions; }
    public void setTotalCO2Emissions(long value) { this.totalCO2Emissions = value; }

    public List<String> getCuisineType() { return cuisineType; }
    public void setCuisineType(List<String> value) { this.cuisineType = value; }

    public Total getTotalNutrients() { return totalNutrients; }
    public void setTotalNutrients(Total value) { this.totalNutrients = value; }

    public long getInflammatoryIndex() { return inflammatoryIndex; }
    public void setInflammatoryIndex(long value) { this.inflammatoryIndex = value; }

    public long getYield() { return yield; }
    public void setYield(long value) { this.yield = value; }

    public List<Digest> getDigest() { return digest; }
    public void setDigest(List<Digest> value) { this.digest = value; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> value) { this.ingredients = value; }

    public Total getTotalDaily() { return totalDaily; }
    public void setTotalDaily(Total value) { this.totalDaily = value; }

    public List<String> getIngredientLines() { return ingredientLines; }
    public void setIngredientLines(List<String> value) { this.ingredientLines = value; }

    public String getImage() { return image; }
    public void setImage(String value) { this.image = value; }

    public Images getImages() { return images; }
    public void setImages(Images value) { this.images = value; }

    public List<String> getCautions() { return cautions; }
    public void setCautions(List<String> value) { this.cautions = value; }

    public List<String> getHealthLabels() { return healthLabels; }
    public void setHealthLabels(List<String> value) { this.healthLabels = value; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String value) { this.externalId = value; }

    public String getLabel() { return label; }
    public void setLabel(String value) { this.label = value; }

    public long getCalories() { return calories; }
    public void setCalories(long value) { this.calories = value; }

    public String geturi() { return uri; }
    public void seturi(String value) { this.uri = value; }

    public String geturl() { return url; }
    public void seturl(String value) { this.url = value; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> value) { this.tags = value; }

    public List<String> getDietLabels() { return dietLabels; }
    public void setDietLabels(List<String> value) { this.dietLabels = value; }

    public List<String> getDishType() { return dishType; }
    public void setDishType(List<String> value) { this.dishType = value; }

    public long getTotalWeight() { return totalWeight; }
    public void setTotalWeight(long value) { this.totalWeight = value; }

    public long getGlycemicIndex() { return glycemicIndex; }
    public void setGlycemicIndex(long value) { this.glycemicIndex = value; }
}

// Digest.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Digest {
    private String schemaOrgTag;
    private String sub;
    private long total;
    private String unit;
    private long daily;
    private boolean hasRDI;
    private String label;
    private String tag;

    public String getSchemaOrgTag() { return schemaOrgTag; }
    public void setSchemaOrgTag(String value) { this.schemaOrgTag = value; }

    public String getSub() { return sub; }
    public void setSub(String value) { this.sub = value; }

    public long getTotal() { return total; }
    public void setTotal(long value) { this.total = value; }

    public String getUnit() { return unit; }
    public void setUnit(String value) { this.unit = value; }

    public long getDaily() { return daily; }
    public void setDaily(long value) { this.daily = value; }

    public boolean getHasRDI() { return hasRDI; }
    public void setHasRDI(boolean value) { this.hasRDI = value; }

    public String getLabel() { return label; }
    public void setLabel(String value) { this.label = value; }

    public String getTag() { return tag; }
    public void setTag(String value) { this.tag = value; }
}

// Images.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Images {
    private Large regular;
    private Large small;
    private Large large;
    private Large thumbnail;

    public Large getRegular() { return regular; }
    public void setRegular(Large value) { this.regular = value; }

    public Large getSmall() { return small; }
    public void setSmall(Large value) { this.small = value; }

    public Large getLarge() { return large; }
    public void setLarge(Large value) { this.large = value; }

    public Large getThumbnail() { return thumbnail; }
    public void setThumbnail(Large value) { this.thumbnail = value; }
}

// Large.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Large {
    private long width;
    private String url;
    private long height;

    public long getWidth() { return width; }
    public void setWidth(long value) { this.width = value; }

    public String geturl() { return url; }
    public void seturl(String value) { this.url = value; }

    public long getHeight() { return height; }
    public void setHeight(long value) { this.height = value; }
}

// Ingredient.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Ingredient {
    private long quantity;
    private String measure;
    private String foodId;
    private long weight;
    private String text;
    private String food;

    public long getQuantity() { return quantity; }
    public void setQuantity(long value) { this.quantity = value; }

    public String getMeasure() { return measure; }
    public void setMeasure(String value) { this.measure = value; }

    public String getFoodId() { return foodId; }
    public void setFoodId(String value) { this.foodId = value; }

    public long getWeight() { return weight; }
    public void setWeight(long value) { this.weight = value; }

    public String getText() { return text; }
    public void setText(String value) { this.text = value; }

    public String getFood() { return food; }
    public void setFood(String value) { this.food = value; }
}

// Total.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Total {
    private AdditionalProp additionalProp1;
    private AdditionalProp additionalProp3;
    private AdditionalProp additionalProp2;

    public AdditionalProp getAdditionalProp1() { return additionalProp1; }
    public void setAdditionalProp1(AdditionalProp value) { this.additionalProp1 = value; }

    public AdditionalProp getAdditionalProp3() { return additionalProp3; }
    public void setAdditionalProp3(AdditionalProp value) { this.additionalProp3 = value; }

    public AdditionalProp getAdditionalProp2() { return additionalProp2; }
    public void setAdditionalProp2(AdditionalProp value) { this.additionalProp2 = value; }
}

// AdditionalProp.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class AdditionalProp {
    private String unit;
    private long quantity;
    private String label;

    public String getUnit() { return unit; }
    public void setUnit(String value) { this.unit = value; }

    public long getQuantity() { return quantity; }
    public void setQuantity(long value) { this.quantity = value; }

    public String getLabel() { return label; }
    public void setLabel(String value) { this.label = value; }
}
