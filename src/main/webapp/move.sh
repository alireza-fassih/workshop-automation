
cd "$(dirname $0)"
echo "$(pwd)"
rm -rfv "../resources/static"
cp -rv "./build" "../resources/static"
mv -fv "../resources/static/index.html" "../resources/templates/dashboard-view.html"