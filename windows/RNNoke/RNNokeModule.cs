using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Noke.RNNoke
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNNokeModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNNokeModule"/>.
        /// </summary>
        internal RNNokeModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNNoke";
            }
        }
    }
}
