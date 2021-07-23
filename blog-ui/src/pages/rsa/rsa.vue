<template>
 <div>

 </div>
</template>

<script>

import { RSAENCRY } from "@/utils/rsa_pri";

export default {
  name: 'rsa',
  mounted () {
    let data = "RSA encrypt!";

    // let encryptDataByPublicKey = RSAENCRY.encryptByPublicKey(data);
    // console.log("encryptDataByPublicKey: " + encryptDataByPublicKey);
    //
    // let decryptDataByPrivateKey = RSAENCRY.decryptByPrivateKey(encryptDataByPublicKey);
    // console.log(("decryptDataByPrivateKey: " + decryptDataByPrivateKey))

    let decryptDataByPublicKey = RSAENCRY.decryptByPublicKey("AwovsO+xeiFSQe6mN9RRCz3FFlMXdCMcrqbNChsc7PbMfCxSHpKWIHQ2IJ/tw8mFsuX84r7TSVhYXB9ATA7nqWxMnKJgkPu+nFXeXSnApjce0MjFtjLpl9wrklVrOL6L0cmzu3DthC44Opwj5EVWF9tjiH7YdKFVK/Q5HVPKcZk=");
    console.log(("decryptDataByPublicKey: " + decryptDataByPublicKey))
  }
};
</script>

<style scoped>

</style>
