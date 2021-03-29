package com.techelevator.tenmo.model;

import java.util.Objects;

public class Authority {

   private String name;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Authority(String name) {
      this.name = name;
   }

   @Override
   public boolean equals(Object authorized) {
      if (this == authorized) return true;
      if (authorized == null || getClass() != authorized.getClass()) return false;
      Authority authority = (Authority) authorized;
      return name == authority.name;
   }

   @Override
   public int hashCode() {
      return Objects.hash(name);
   }

   @Override
   public String toString() {
      return "Authority{" +
         "name=" + name +
         '}';
   }
}
