from fabric.api import run, sudo, local
from fabric.operations import put
from fabric.context_managers import cd, lcd

def deploy():
    with lcd('vue'):
        local('npm run build')
    local('activator clean dist')
    with cd('vue-stocks'):
        sudo('systemctl stop vue-stocks')
        sudo('rm -rf vue_stocks-1.0*')
        put('target/universal/vue_stocks-1.0.zip', 'vue_stocks-1.0.zip')
        run('unzip vue_stocks-1.0.zip')
        sudo('systemctl start vue-stocks')
