FROM ghcr.io/hombre-x/silicon-emporium-backend:0.1.11

HEALTHCHECK --interval=5m --timeout=3s \
  CMD curl -f http://localhost/hello/Alive || exit 1