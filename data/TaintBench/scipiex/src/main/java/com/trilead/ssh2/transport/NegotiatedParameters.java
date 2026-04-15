package com.trilead.ssh2.transport;

public class NegotiatedParameters {
    public String comp_algo_client_to_server;
    public String comp_algo_server_to_client;
    public String enc_algo_client_to_server;
    public String enc_algo_server_to_client;
    public boolean guessOK;
    public String kex_algo;
    public String lang_client_to_server;
    public String lang_server_to_client;
    public String mac_algo_client_to_server;
    public String mac_algo_server_to_client;
    public String server_host_key_algo;
}
