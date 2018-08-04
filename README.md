# vertx-ssl-demo

1. Create keystore using Java keytool:

```.\keytool.exe -genkey -alias localhost -keyalg RSA -keystore C:\PATH\TO\keystore.jks -storepass password```

2. Put keystore in classpath and set property *keystore-file-name*.

3. Verify working encrypted communication using tools like *Wireshark* or *RawCap*.
