use refactored_se2;
UPDATE recipe
SET
  imagePath = CONCAT(
    '/groupf/recipeapp/images/',
    REPLACE(name, ' ', '_'),
    '_image.png'
  );