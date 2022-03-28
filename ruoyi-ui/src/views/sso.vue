<template>
<div/>
</template>

<script>
import {getQueryObject, getQueryString} from '@/utils'
import Cookies from "js-cookie";

export default {
  name: 'sso',
  data() {
    return {
      loginForm: {
        ssoCode: "",
      }
    };
  },
  created() {
    Cookies.remove('Admin-Token')
    const url = window.location.href
    let queryObject = getQueryObject(url)
    this.loginForm.ssoCode = queryObject.code
    this.$store.dispatch("LoginBySSO", this.loginForm)
        .then(() => {this.$router.push({path: this.redirect || "/"});})
  }
}
</script>
