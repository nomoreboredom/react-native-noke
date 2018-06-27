
import { NativeModules } from 'react-native';

const { RNNoke } = NativeModules;

export default {
  ...RNNoke,
  getProducts: (page = 1, collectionId, tags) => {
    return true
    // if (collectionId) {
    //   return RNShopify.getProductsWithTagsForCollection(page, collectionId, tags);
    // }
    // return tags ? RNShopify.getProductsWithTags(page, tags) : RNShopify.getProductsPage(page);
  }
};
