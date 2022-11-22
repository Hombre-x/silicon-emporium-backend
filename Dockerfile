FROM ghcr.io/hombre-x/silicon-emporium-backend:0.1.6

HEALTHCHECK --interval=5m --timeout=3s \
  CMD curl -f http://localhost/ || exit 1