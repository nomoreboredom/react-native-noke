
import { NativeModules } from 'react-native';

const { RNNoke } = NativeModules;

export default {
  ...RNNoke,
  initialize: () => {
    return RNNoke.init()
    // if (collectionId) {
    //   return RNShopify.getProductsWithTagsForCollection(page, collectionId, tags);
    // }
    // return tags ? RNShopify.getProductsWithTags(page, tags) : RNShopify.getProductsPage(page);
  },
  connect: (noke) => {
    return RNNoke.connectToNoke(noke)
  },
  sendAction: (noke) => {
    return RNNoke.sendCommand(noke)
  }
};
