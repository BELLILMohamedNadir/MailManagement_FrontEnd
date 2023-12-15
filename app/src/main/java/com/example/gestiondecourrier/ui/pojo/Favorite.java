package com.example.gestiondecourrier.ui.pojo;

public class Favorite {

    private FavoritePk favoritePk;
    private boolean favorite;

    public Favorite() {
    }

    public Favorite(FavoritePk favoritePk, boolean favorite) {
        this.favoritePk = favoritePk;
        this.favorite = favorite;
    }

    public FavoritePk getFavoritePk() {
        return favoritePk;
    }

    public void setFavoritePk(FavoritePk favoritePk) {
        this.favoritePk = favoritePk;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
