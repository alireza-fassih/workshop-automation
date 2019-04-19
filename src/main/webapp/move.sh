
cd "$(dirname $0)"
echo "$(pwd)"
rm -rf "../resources/static/*"
cp -r "./build/*" "../resources/static/"
mv -y "../resources/static/index.html" "../resources/templates/dashboard-view.html"