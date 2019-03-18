package com.example.pikot.sugophapp.RandomClasses;


 public class Category {
        private String categoryId;
        private String categoryName;
        private String categoryDescription;

        public Category(String name, String categoryDescription, String categoryId) {
            this.categoryName = name;
            this.categoryDescription= categoryDescription;
            this.categoryId= categoryId;

        }

        public String getCategoryName() {
            return this.categoryName;
        }

        public String getCategoryDescription() {
            return this.categoryDescription;
        }

         public String getCategoryId() {
             return this.categoryId;
         }
    }

